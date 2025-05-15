import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { RentService } from '../rent.service';
import { Rent } from '../model/Rent';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { DateAdapter, MAT_DATE_FORMATS, MAT_DATE_LOCALE, MAT_NATIVE_DATE_FORMATS, MatNativeDateModule, NativeDateAdapter } from '@angular/material/core';
import { Client } from '../../client/model/Client';
import { Game } from '../../game/model/Game';
import { GameService } from '../../game/game.service';
import { ClientService } from '../../client/client.service';
import { MatSelectModule } from '@angular/material/select';

@Component({
    selector: 'app-rent-edit',
    standalone: true,
    imports: [FormsModule, ReactiveFormsModule, MatFormFieldModule, MatInputModule, MatButtonModule, MatDatepickerModule, MatNativeDateModule, MatSelectModule],
    templateUrl: './rent-edit.component.html',
    styleUrl: './rent-edit.component.scss',
    providers: [ {provide: DateAdapter, useClass: NativeDateAdapter}, {provide: MAT_DATE_FORMATS, useValue: MAT_NATIVE_DATE_FORMATS}, { provide: MAT_DATE_LOCALE, useValue: 'en-GB' } ],
})
export class RentEditComponent implements OnInit {
    rent: Rent;
    clients: Client[];
    games: Game[];

    constructor(
        public dialogRef: MatDialogRef<RentEditComponent>,
        @Inject(MAT_DIALOG_DATA) public data: any,
        private rentService: RentService,
        private gameService: GameService,
        private clientService: ClientService,
    ) {}

    ngOnInit(): void {
        this.rent = this.data.rent ? Object.assign({}, this.data.rent) : new Rent();

        this.clientService.getClients().subscribe((clients) => {
            this.clients = clients;

            if (this.rent.client != null) {
                const clientFilter: Client[] = clients.filter(
                    (client) => client.id == this.data.rent.client.id
                );
                if (clientFilter != null) {
                    this.rent.client = clientFilter[0];
                }
            }
        });

        this.gameService.getGames().subscribe((games) => {
            this.games = games;

            if (this.rent.game != null) {
                const gameFilter: Game[] = games.filter(
                    (game) => game.id == this.data.rent.game.id
                );
                if (gameFilter != null) {
                    this.rent.game = gameFilter[0];
                }
            }
        });
    }

    onSave() {
        const startDate = this.rent.startDate ? new Date(this.rent.startDate) : null;
        const endDate = this.rent.endDate ? new Date(this.rent.endDate) : null;
    
        if (startDate && endDate) {
            
            if (startDate > endDate) {
                alert('La fecha de fin no puede ser anterior a la fecha de inicio.');
                return;
            }
    
            const diffInDays = (endDate.getTime() - startDate.getTime()) / (1000 * 60 * 60 * 24);
            if (diffInDays > 14) {
                alert('El préstamo no puede exceder los 14 días.');
                return;
            }
    
            this.saveRent();
        }
    }
    
    private saveRent(): void {
        this.rent.startDate = this.formatDateToLocal(new Date(this.rent.startDate)) as any;
        this.rent.endDate = this.formatDateToLocal(new Date(this.rent.endDate)) as any;

        this.rentService.saveRent(this.rent).subscribe({
            next: () => this.dialogRef.close(),
            error: (error) => {
                const backendMsg = error?.error?.message || error?.error || 'Error al guardar el préstamo.';
                alert(backendMsg);
            }
        });
    }

    onClose() {
        this.dialogRef.close();
    }

    private formatDateToLocal(date: Date): string {
        const year = date.getFullYear();
        const month = (date.getMonth() + 1).toString().padStart(2, '0');
        const day = date.getDate().toString().padStart(2, '0');
        return `${year}-${month}-${day}`;
    }
}