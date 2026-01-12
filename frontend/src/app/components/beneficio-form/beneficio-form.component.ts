import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';
import { BeneficioService } from '../../services/beneficio.service';
import { Beneficio } from '../../models/beneficio.model';

@Component({
    selector: 'app-beneficio-form',
    templateUrl: './beneficio-form.component.html',
    styleUrls: ['./beneficio-form.component.css']
})
export class BeneficioFormComponent implements OnInit {
    form!: FormGroup;
    isEditMode = false;
    beneficioId: number | null = null;
    loading = false;
    submitting = false;

    constructor(
        private fb: FormBuilder,
        private beneficioService: BeneficioService,
        private router: Router,
        private route: ActivatedRoute,
        private snackBar: MatSnackBar
    ) { }

    ngOnInit(): void {
        this.initForm();
        this.checkEditMode();
    }

    initForm(): void {
        this.form = this.fb.group({
            nome: ['', [Validators.required, Validators.maxLength(100)]],
            descricao: ['', Validators.maxLength(255)],
            valor: [null, [Validators.required, Validators.min(0.01)]],
            ativo: [true, Validators.required]
        });
    }

    checkEditMode(): void {
        const id = this.route.snapshot.paramMap.get('id');
        if (id) {
            this.isEditMode = true;
            this.beneficioId = parseInt(id, 10);
            this.loadBeneficio();
        }
    }

    loadBeneficio(): void {
        if (this.beneficioId) {
            console.debug(`Carregando benefício para edição. ID: ${this.beneficioId}`);
            this.loading = true;
            this.beneficioService.findById(this.beneficioId).subscribe({
                next: (data) => {
                    this.form.patchValue(data);
                    this.loading = false;
                },
                error: (err) => {
                    this.showError('Erro ao carregar benefício: ' + (err.error?.message || err.message));
                    this.loading = false;
                }
            });
        }
    }

    onSubmit(): void {
        if (this.form.valid) {
            this.submitting = true;

            const beneficio: Beneficio = this.form.value;

            const request = this.isEditMode && this.beneficioId
                ? this.beneficioService.update(this.beneficioId, beneficio)
                : this.beneficioService.create(beneficio);

            request.subscribe({
                next: () => {
                    this.showSuccess(this.isEditMode ? 'Benefício atualizado com sucesso!' : 'Benefício criado com sucesso!');
                    this.router.navigate(['/beneficios']);
                },
                error: (err) => {
                    this.showError('Erro ao salvar benefício: ' + (err.error?.message || err.message));
                    this.submitting = false;
                }
            });
        }
    }

    cancel(): void {
        this.router.navigate(['/beneficios']);
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
}
