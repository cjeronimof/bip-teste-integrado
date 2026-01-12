import { Directive, ElementRef, HostListener, forwardRef } from '@angular/core';
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from '@angular/forms';

@Directive({
    selector: '[appCurrencyMask]',
    providers: [
        {
            provide: NG_VALUE_ACCESSOR,
            useExisting: forwardRef(() => CurrencyMaskDirective),
            multi: true
        }
    ]
})
export class CurrencyMaskDirective implements ControlValueAccessor {

    private onChange: (value: any) => void = () => { };
    private onTouched: () => void = () => { };

    constructor(private el: ElementRef) { }

    // ControlValueAccessor Interface Implementation

    writeValue(value: any): void {
        this.el.nativeElement.value = this.format(value);
    }

    registerOnChange(fn: any): void {
        this.onChange = fn;
    }

    registerOnTouched(fn: any): void {
        this.onTouched = fn;
    }

    setDisabledState(isDisabled: boolean): void {
        this.el.nativeElement.disabled = isDisabled;
    }

    // Event Handlers

    @HostListener('input', ['$event.target.value'])
    onInput(value: string) {
        // Format input immediately for visual feedback
        const formatted = this.format(value);
        this.el.nativeElement.value = formatted;

        // Parse to float and notify Angular form control
        const floatValue = this.parse(formatted);
        this.onChange(floatValue);
    }

    @HostListener('blur')
    onBlur() {
        this.onTouched();
    }

    // Helper Methods

    private format(value: any): string {
        if (value === null || value === undefined || value === '') {
            return '';
        }

        // 1. Clean non-numeric characters
        let v = value.toString().replace(/\D/g, '');

        // 2. Handle empty after clean
        if (v.length === 0) {
            return '';
        }

        // 3. Ensure at least 3 digits for cents styling (e.g. 1 -> 0,01)
        v = v.padStart(3, '0');

        // 4. Extract integer and decimal parts
        // "123" -> 1,23
        const len = v.length;
        const integerPart = v.slice(0, len - 2);
        const decimalPart = v.slice(len - 2);

        // 5. Remove leading zeros from integer part, but keep single '0' if empty
        let integerPartClean = integerPart.replace(/^0+(?=\d)/, '');
        if (integerPartClean === '') integerPartClean = '0';

        // 6. Add thousands separator (dot)
        const integerFormatted = integerPartClean.replace(/\B(?=(\d{3})+(?!\d))/g, '.');

        // 7. Combine
        return `R$ ${integerFormatted},${decimalPart}`;
    }

    private parse(formattedValue: string): number | null {
        if (!formattedValue) return null;

        // "R$ 1.234,56" -> "1234.56"
        const cleanValues = formattedValue.replace(/\D/g, '');
        if (!cleanValues) return null;

        // Insert decimal point at correct position (last 2 digits are cents)
        const len = cleanValues.length;
        const integerPart = cleanValues.slice(0, len - 2);
        const decimalPart = cleanValues.slice(len - 2);

        return parseFloat(`${integerPart}.${decimalPart}`);
    }
}
