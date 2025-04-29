import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Client } from './model/Client';
import { HttpClient } from '@angular/common/http';
import { map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class ClientService {

  constructor(
    private http: HttpClient
  ) { }

  private baseUrl = 'http://localhost:8080/client';

  getClients(): Observable<Client[]> {
    return this.http.get<Client[]>(this.baseUrl).pipe(
      map(clients => clients.sort((a, b) => a.id - b.id)) // Ordenar por ID de menor a mayor
    );
  }

  saveClient(client: Client): Observable<Client> {
    const { id } = client;
    const url = id ? `${this.baseUrl}/${id}` : this.baseUrl;
    return this.http.put<Client>(url, client);
  }

  deleteClient(idClient : number): Observable<any> {
    return this.http.delete(`${this.baseUrl}/${idClient}`);
} 
}
