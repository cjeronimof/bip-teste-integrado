import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';
import { BeneficioService } from '../../services/beneficio.service';
import { Beneficio } from '../../models/beneficio.model';

@Component({
    selector: 'app-beneficio-list',
    templateUrl: './beneficio-list.component.html',
    styleUrls: ['./beneficio-list.component.css']
})
export class BeneficioListComponent implements OnInit {
    beneficios: Beneficio[] = [];
    displayedColumns: string[] = ['id', 'nome', 'descricao', 'valor', 'ativo', 'acoes'];
    loading = false;

    constructor(
        private beneficioService: BeneficioService,
        private router: Router,
        private snackBar: MatSnackBar
    ) { }

    ngOnInit(): void {
        this.loadBeneficios();
    }

    loadBeneficios(): void {
        console.debug('Carregando lista de benefícios');
        this.loading = true;

        this.beneficioService.findAll().subscribe({
            next: (data) => {
                this.beneficios = data;
                this.loading = false;
                console.debug(`Total de benefícios carregados: ${data.length}`);
            },
            error: (err) => {
                this.showError('Erro ao carregar benefícios: ' + (err.error?.message || err.message));
                this.loading = false;
            }
        });
    }

    navigateToNew(): void {
        this.router.navigate(['/beneficios/novo']);
    }

    navigateToEdit(id: number): void {
        this.router.navigate(['/beneficios', id, 'editar']);
    }

    navigateToTransferencia(): void {
        this.router.navigate(['/transferencia']);
    }

    deleteBeneficio(id: number, nome: string): void {
        if (confirm(`Tem certeza que deseja deletar o benefício "${nome}"?`)) {
            this.loading = true;
            this.beneficioService.delete(id).subscribe({
                next: () => {
                    this.showSuccess('Benefício deletado com sucesso!');
                    this.loadBeneficios();
                },
                error: (err) => {
                    this.showError('Erro ao deletar benefício: ' + (err.error?.message || err.message));
                    this.loading = false;
                }
            });
        }
    }

    formatCurrency(value: number): string {
        return new Intl.NumberFormat('pt-BR', {
            style: 'currency',
            currency: 'BRL'
        }).format(value);
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
