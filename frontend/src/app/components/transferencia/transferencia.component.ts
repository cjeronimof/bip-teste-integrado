import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';
import { BeneficioService } from '../../services/beneficio.service';
import { Beneficio, Transferencia } from '../../models/beneficio.model';

@Component({
    selector: 'app-transferencia',
    templateUrl: './transferencia.component.html',
    styleUrls: ['./transferencia.component.css']
})
export class TransferenciaComponent implements OnInit {
    form!: FormGroup;
    beneficios: Beneficio[] = [];
    loading = false;
    submitting = false;
    error: string | null = null;
    successMessage: string | null = null;

    constructor(
        private fb: FormBuilder,
        private beneficioService: BeneficioService,
        private router: Router,
        private snackBar: MatSnackBar
    ) { }

    ngOnInit(): void {
        this.initForm();
        this.loadBeneficios();
    }

    initForm(): void {
        this.form = this.fb.group({
            origemId: [null, Validators.required],
            destinoId: [null, Validators.required],
            valor: [null, [Validators.required, Validators.min(0.01)]]
        });
    }

    loadBeneficios(): void {
        this.loading = true;
        this.beneficioService.findAll().subscribe({
            next: (data) => {
                this.beneficios = data.filter(b => b.ativo);
                this.loading = false;
            },
            error: (err) => {
                this.showError('Erro ao carregar benefícios: ' + (err.error?.message || err.message));
                this.loading = false;
            }
        });
    }

    onSubmit(): void {
        if (this.form.valid) {
            const transferencia: Transferencia = this.form.value;

            if (transferencia.origemId === transferencia.destinoId) {
                this.showError('Origem e destino não podem ser o mesmo benefício');
                return;
            }

            console.info(`Submetendo transferência de ${transferencia.origemId} para ${transferencia.destinoId}`);
            this.submitting = true;

            this.beneficioService.transferir(transferencia).subscribe({
                next: () => {
                    setTimeout(() => {
                        this.showSuccess('Transferência realizada com sucesso!');
                        this.router.navigate(['/beneficios']);
                    }, 500);
                },
                error: (err) => {
                    this.showError('Erro na transferência: ' + (err.error?.message || err.message));
                    this.submitting = false;
                }
            });
        }
    }

    private showSuccess(message: string): void {
        this.snackBar.open(message, 'Fechar', {
            duration: 3000,
            panelClass: ['success-snackbar']
        });
    }

    private showError(message: string): void {
        this.snackBar.open(message, 'Fechar', {
            duration: 5000,
            panelClass: ['error-snackbar']
        });
    }

    markFormGroupTouched(formGroup: FormGroup): void {
        Object.keys(formGroup.controls).forEach(key => {
            const control = formGroup.get(key);
            control?.markAsTouched();
        });
    }

    cancel(): void {
        this.router.navigate(['/beneficios']);
    }

    getBeneficioById(id: number): Beneficio | undefined {
        return this.beneficios.find(b => b.id === id);
    }

    getSaldoOrigemPrevisto(): string {
        const origemId = this.form.get('origemId')?.value;
        const valorTransferencia = this.form.get('valor')?.value || 0;

        if (origemId) {
            const beneficio = this.getBeneficioById(origemId);
            if (beneficio) {
                const novoSaldo = beneficio.valor - valorTransferencia;
                return this.formatCurrency(novoSaldo);
            }
        }
        return '-';
    }

    getSaldoDestinoPrevisto(): string {
        const destinoId = this.form.get('destinoId')?.value;
        const valorTransferencia = this.form.get('valor')?.value || 0;

        if (destinoId) {
            const beneficio = this.getBeneficioById(destinoId);
            if (beneficio) {
                const novoSaldo = beneficio.valor + valorTransferencia;
                return this.formatCurrency(novoSaldo);
            }
        }
        return '-';
    }

    formatCurrency(value: number): string {
        return new Intl.NumberFormat('pt-BR', {
            style: 'currency',
            currency: 'BRL'
        }).format(value);
    }

    isFieldInvalid(fieldName: string): boolean {
        const field = this.form.get(fieldName);
        return !!(field && field.invalid && field.touched);
    }

    getFieldError(fieldName: string): string {
        const field = this.form.get(fieldName);
        if (field?.hasError('required')) {
            return 'Campo obrigatório';
        }
        if (field?.hasError('min')) {
            return 'Valor deve ser maior que zero';
        }
        return '';
    }
}
