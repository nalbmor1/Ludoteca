import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { Pageable } from '../core/model/page/Pageable';
import { Rent } from './model/Rent';
import { RentPage } from './model/RentPage';
import { RENT_DATA } from './model/mock-rent';
import { HttpClient } from '@angular/common/http';

@Injectable({
    providedIn: 'root',
})
export class RentService {
    constructor(private http: HttpClient) {}

    private baseUrl = 'http://localhost:8080/rent';

    getRents(pageable: Pageable): Observable<RentPage> {
        return this.http.post<RentPage>(this.baseUrl, { pageable: pageable });
    }

    saveRent(rent: Rent): Observable<Rent> {
        const { id } = rent;
        const url = id ? `${this.baseUrl}/${id}` : this.baseUrl;
        return this.http.put<Rent>(url, rent);
    }

    deleteRent(idRent: number): Observable<void> {
        return this.http.delete<void>(`${this.baseUrl}/${idRent}`);
    }
}