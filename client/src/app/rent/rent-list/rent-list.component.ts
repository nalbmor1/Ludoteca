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

@Component({
    selector: 'app-rent-list',
    standalone: true,
    imports: [MatButtonModule, MatIconModule, MatTableModule, CommonModule, MatPaginator],
    templateUrl: './rent-list.component.html',
    styleUrl: './rent-list.component.scss',
})
export class RentListComponent implements OnInit {
    pageNumber: number = 0;
    pageSize: number = 5;
    totalElements: number = 0;

    dataSource = new MatTableDataSource<Rent>();
    displayedColumns: string[] = ['id', 'game', 'client', 'startDate', 'endDate', 'action'];

    constructor(private rentService: RentService, public dialog: MatDialog) {}

    ngOnInit(): void {
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

        this.rentService.getRents(pageable).subscribe((data) => {
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