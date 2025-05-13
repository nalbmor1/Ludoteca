import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { Pageable } from '../core/model/page/Pageable';
import { Rent } from './model/Rent';
import { RentPage } from './model/RentPage';
import { HttpClient } from '@angular/common/http';
import { forkJoin, map } from 'rxjs';

@Injectable({
    providedIn: 'root',
})
export class RentService {
    constructor(private http: HttpClient) {}

    private baseUrl = 'http://localhost:8080/rent';

    getRents(pageable: Pageable, gameId?: number, clientId?: number, selectedDate?: string): Observable<RentPage> {
        const params: string[] = [];
        if (gameId) {
            params.push(`game=${gameId}`);
        }
        if (clientId) {
            params.push(`client=${clientId}`);
        }
        if (selectedDate) {
            params.push(`selectedDate=${selectedDate}`);
        }
        const queryString = params.length > 0 ? `?${params.join('&')}` : '';
        const url = `${this.baseUrl}${queryString}`;
        return this.http.post<RentPage>(url, { pageable });
    }

    saveRent(rent: Rent): Observable<Rent> {
        const { id } = rent;
        const url = id ? `${this.baseUrl}/${id}` : this.baseUrl;
        return this.http.put<Rent>(url, rent);
    }

    deleteRent(idRent: number): Observable<void> {
        return this.http.delete<void>(`${this.baseUrl}/${idRent}`);
    }

    getRentsForRange(pageable: Pageable, gameId: number, startDate: string, endDate: string) {
        return this.getRents(pageable, gameId).pipe(
            map(page => {
                return page.content.filter(rent => {
                    const rentStart = new Date(rent.startDate);
                    const rentEnd = new Date(rent.endDate);
                    
                    return new Date(startDate) <= rentEnd && new Date(endDate) >= rentStart;
                });
            })
        );
    }
}