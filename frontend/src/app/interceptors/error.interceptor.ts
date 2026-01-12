import { Injectable } from '@angular/core';
import {
    HttpRequest,
    HttpHandler,
    HttpEvent,
    HttpInterceptor,
    HttpErrorResponse
} from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { MatSnackBar } from '@angular/material/snack-bar';

@Injectable()
export class ErrorInterceptor implements HttpInterceptor {

    constructor(private snackBar: MatSnackBar) { }

    intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
        return next.handle(request).pipe(
            catchError((error: HttpErrorResponse) => {
                let message = 'Ocorreu um erro inesperado';

                console.error('HTTP Error identified:', error);

                if (error.error instanceof ErrorEvent) {
                    message = `Erro: ${error.error.message}`;
                } else {
                    if (error.error && error.error.message) {
                        message = error.error.message;

                        if (error.error.validationErrors) {
                            const details = error.error.validationErrors.map((e: any) => e.message).join(', ');
                            message = `${message}: ${details}`;
                        }
                    } else {
                        message = `Erro ${error.status}: ${error.statusText}`;
                    }
                }

                this.snackBar.open(message, 'Fechar', {
                    duration: 5000,
                    panelClass: ['error-snackbar'],
                    horizontalPosition: 'right',
                    verticalPosition: 'top'
                });

                return throwError(() => error);
            })
        );
    }
}
