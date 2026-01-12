import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Beneficio, Transferencia } from '../models/beneficio.model';

@Injectable({
    providedIn: 'root'
})
export class BeneficioService {
    private apiUrl = 'http://localhost:8080/api/beneficios';

    constructor(private http: HttpClient) { }

    findAll(): Observable<Beneficio[]> {
        console.debug('Fetching all benefits');
        return this.http.get<Beneficio[]>(this.apiUrl);
    }

    findById(id: number): Observable<Beneficio> {
        console.debug(`Fetching benefit by ID: ${id}`);
        return this.http.get<Beneficio>(`${this.apiUrl}/${id}`);
    }

    create(beneficio: Beneficio): Observable<Beneficio> {
        console.info(`Creating benefit: ${beneficio.nome}`);
        return this.http.post<Beneficio>(this.apiUrl, beneficio);
    }

    update(id: number, beneficio: Beneficio): Observable<Beneficio> {
        console.info(`Updating benefit ID: ${id}`);
        return this.http.put<Beneficio>(`${this.apiUrl}/${id}`, beneficio);
    }

    delete(id: number): Observable<void> {
        console.info(`Deleting benefit ID: ${id}`);
        return this.http.delete<void>(`${this.apiUrl}/${id}`);
    }

    transferir(transferencia: Transferencia): Observable<void> {
        console.info(`Initiating transfer from ${transferencia.origemId} to ${transferencia.destinoId}`);
        return this.http.post<void>(`${this.apiUrl}/transferencia`, transferencia);
    }
}
