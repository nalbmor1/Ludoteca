import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { PageEvent } from '@angular/material/paginator';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { RentEditComponent } from '../rent-edit/rent-edit.component';
import { RentService } from '../rent.service';
import { Rent } from '../model/Rent';
import { Pageable } from '../../core/model/page/Pageable';
import { DialogConfirmationComponent } from '../../core/dialog-confirmation/dialog-confirmation.component';
import { CommonModule } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatPaginator } from '@angular/material/paginator';
import { MatInputModule } from '@angular/material/input';
import { FormsModule } from '@angular/forms';
import { Game } from '../../game/model/Game';
import { Client } from '../../client/model/Client';
import { GameService } from '../../game/game.service';
import { ClientService } from '../../client/client.service';
import { MatSelectModule } from '@angular/material/select';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';

@Component({
    selector: 'app-rent-list',
    standalone: true,
    imports: [MatButtonModule, MatIconModule, MatTableModule, CommonModule, MatPaginator, MatInputModule, FormsModule, MatSelectModule, MatDatepickerModule, MatNativeDateModule],
    templateUrl: './rent-list.component.html',
    styleUrl: './rent-list.component.scss',
})
export class RentListComponent implements OnInit {
    pageNumber: number = 0;
    pageSize: number = 5;
    totalElements: number = 0;

    games: Game[];
    clients: Client[];
    filterGame: Game;
    filterClient: Client;
    filterDate: Date;

    dataSource = new MatTableDataSource<Rent>();
    displayedColumns: string[] = ['id', 'game', 'client', 'startDate', 'endDate', 'action'];

    constructor(private rentService: RentService, public dialog: MatDialog, private gameService: GameService, private clientService: ClientService) {}

    ngOnInit(): void {
        this.loadPage();
        this.gameService.getGames().subscribe((games) => (this.games = games));
        this.clientService.getClients().subscribe((clients) => (this.clients = clients));
    }

    onCleanFilter(): void {
        this.filterGame = null;
        this.filterClient = null;
        this.filterDate = null;
        this.onSearch();
    }

    onSearch(): void {
        this.loadPage();
    }

    loadPage(event?: PageEvent) {
        const pageable: Pageable = {
            pageNumber: this.pageNumber,
            pageSize: this.pageSize,
            sort: [
                {
                    property: 'id',
                    direction: 'ASC',
                },
            ],
        };

        if (event != null) {
            pageable.pageSize = event.pageSize;
            pageable.pageNumber = event.pageIndex;
        }

        const gameId = this.filterGame != null ? this.filterGame.id : null;
        const clientId = this.filterClient != null ? this.filterClient.id : null;
        const selectedDate = this.filterDate ? this.filterDate.toISOString().split('T')[0] : null;

        this.rentService.getRents(pageable, gameId, clientId, selectedDate).subscribe((data) => {
            this.dataSource.data = data.content;
            this.pageNumber = data.pageable.pageNumber;
            this.pageSize = data.pageable.pageSize;
            this.totalElements = data.totalElements;
        });
    }

    createRent() {
        const dialogRef = this.dialog.open(RentEditComponent, {
            data: {},
        });

        dialogRef.afterClosed().subscribe((result) => {
            this.ngOnInit();
        });
    }

    editRent(rent: Rent) {
        const dialogRef = this.dialog.open(RentEditComponent, {
            data: { rent: rent },
        });

        dialogRef.afterClosed().subscribe((result) => {
            this.ngOnInit();
        });
    }

    deleteRent(rent: Rent) {
        const dialogRef = this.dialog.open(DialogConfirmationComponent, {
            data: {
                title: 'Eliminar préstamos',
                description:
                    'Atención si borra el préstamo se perderán sus datos.<br> ¿Desea eliminar el préstamo?',
            },
        });

        dialogRef.afterClosed().subscribe((result) => {
            if (result) {
                this.rentService.deleteRent(rent.id).subscribe((result) => {
                    this.ngOnInit();
                });
            }
        });
    }
}