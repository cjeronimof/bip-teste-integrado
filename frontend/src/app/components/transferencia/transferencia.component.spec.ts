import { ComponentFixture, TestBed } from '@angular/core/testing';
import { TransferenciaComponent } from './transferencia.component';
import { BeneficioService } from '../../services/beneficio.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { of, throwError } from 'rxjs';
import { Router } from '@angular/router';
import { CurrencyMaskDirective } from '../../directives/currency-mask.directive';

describe('TransferenciaComponent', () => {
    let component: TransferenciaComponent;
    let fixture: ComponentFixture<TransferenciaComponent>;
    let beneficioServiceSpy: jasmine.SpyObj<BeneficioService>;
    let snackBarSpy: jasmine.SpyObj<MatSnackBar>;
    let routerSpy: jasmine.SpyObj<Router>;

    beforeEach(async () => {
        beneficioServiceSpy = jasmine.createSpyObj('BeneficioService', ['findAll', 'transferir']);
        snackBarSpy = jasmine.createSpyObj('MatSnackBar', ['open']);
        routerSpy = jasmine.createSpyObj('Router', ['navigate']);

        await TestBed.configureTestingModule({
            declarations: [TransferenciaComponent, CurrencyMaskDirective],
            imports: [
                ReactiveFormsModule,
                MatCardModule,
                MatInputModule,
                MatSelectModule,
                MatButtonModule,
                MatFormFieldModule,
                MatIconModule,
                MatProgressSpinnerModule,
                BrowserAnimationsModule
            ],
            providers: [
                { provide: BeneficioService, useValue: beneficioServiceSpy },
                { provide: MatSnackBar, useValue: snackBarSpy },
                { provide: Router, useValue: routerSpy }
            ]
        })
            .compileComponents();
    });

    beforeEach(() => {
        fixture = TestBed.createComponent(TransferenciaComponent);
        component = fixture.componentInstance;
        beneficioServiceSpy.findAll.and.returnValue(of([
            { id: 1, nome: 'Origem', descricao: 'Desc 1', valor: 1000, ativo: true },
            { id: 2, nome: 'Destino', descricao: 'Desc 2', valor: 500, ativo: true }
        ]));
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });

    it('should load benefits on init', () => {
        expect(beneficioServiceSpy.findAll).toHaveBeenCalled();
        expect(component.beneficios.length).toBe(2);
    });

    it('should show error when transfer fails', () => {
        const errorResponse = { error: { message: 'Unable to locate entity descriptor' } };
        beneficioServiceSpy.transferir.and.returnValue(throwError(() => errorResponse));

        // Fix form structure matching
        component.form.patchValue({
            origemId: 1,
            destinoId: 2,
            valor: 100
        });

        component.onSubmit();

        expect(beneficioServiceSpy.transferir).toHaveBeenCalled();
        expect(snackBarSpy.open).toHaveBeenCalledWith(
            jasmine.stringMatching(/Erro na transferência:.*Unable to locate entity descriptor/),
            'Fechar',
            jasmine.any(Object)
        );
    });

    it('should prevent transfer to same benefit', () => {
        component.form.patchValue({
            origemId: 1,
            destinoId: 1,
            valor: 100
        });

        component.onSubmit();

        expect(beneficioServiceSpy.transferir).not.toHaveBeenCalled();
        expect(snackBarSpy.open).toHaveBeenCalledWith(
            'Origem e destino não podem ser o mesmo benefício',
            'Fechar',
            jasmine.any(Object)
        );
    });
});
