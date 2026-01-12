import { NgModule, LOCALE_ID } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { ReactiveFormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { registerLocaleData } from '@angular/common';
import ptBr from '@angular/common/locales/pt';

import { MatToolbarModule } from '@angular/material/toolbar';
import { MatCardModule } from '@angular/material/card';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatSelectModule } from '@angular/material/select';
import { MatIconModule } from '@angular/material/icon';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { MatTableModule } from '@angular/material/table';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatChipsModule } from '@angular/material/chips';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatCheckboxModule } from '@angular/material/checkbox';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BeneficioListComponent } from './components/beneficio-list/beneficio-list.component';
import { BeneficioFormComponent } from './components/beneficio-form/beneficio-form.component';
import { TransferenciaComponent } from './components/transferencia/transferencia.component';
import { BeneficioService } from './services/beneficio.service';
import { CurrencyMaskDirective } from './directives/currency-mask.directive';
import { ErrorInterceptor } from './interceptors/error.interceptor';

registerLocaleData(ptBr);

@NgModule({
  declarations: [
    AppComponent,
    BeneficioListComponent,
    BeneficioFormComponent,
    TransferenciaComponent,
    CurrencyMaskDirective
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    AppRoutingModule,
    HttpClientModule,
    ReactiveFormsModule,
    RouterModule,
    MatToolbarModule,
    MatCardModule,
    MatInputModule,
    MatButtonModule,
    MatSelectModule,
    MatIconModule,
    MatSnackBarModule,
    MatTableModule,
    MatProgressSpinnerModule,
    MatChipsModule,
    MatTooltipModule,
    MatCheckboxModule
  ],
  providers: [
    BeneficioService,
    { provide: LOCALE_ID, useValue: 'pt-BR' },
    { provide: HTTP_INTERCEPTORS, useClass: ErrorInterceptor, multi: true }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
