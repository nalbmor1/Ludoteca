import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { Pageable } from '../core/model/page/Pageable';
import { Rent } from './model/Rent';
import { RentPage } from './model/RentPage';
import { RENT_DATA } from './model/mock-rent';

@Injectable({
    providedIn: 'root',
})
export class RentService {
    constructor() {}

    getRents(pageable: Pageable): Observable<RentPage> {
        return of(RENT_DATA);
    }

    saveRent(rent: Rent): Observable<void> {
        return of(null);
    }

    deleteRent(idRent: number): Observable<void> {
        return of(null);
    }
}