import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { RentService } from '../rent.service';
import { Rent } from '../model/Rent';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { DateAdapter, MAT_DATE_FORMATS, MAT_NATIVE_DATE_FORMATS, MatNativeDateModule, NativeDateAdapter } from '@angular/material/core';

@Component({
    selector: 'app-rent-edit',
    standalone: true,
    imports: [FormsModule, ReactiveFormsModule, MatFormFieldModule, MatInputModule, MatButtonModule, MatDatepickerModule, MatNativeDateModule],
    templateUrl: './rent-edit.component.html',
    styleUrl: './rent-edit.component.scss',
    providers: [ {provide: DateAdapter, useClass: NativeDateAdapter}, {provide: MAT_DATE_FORMATS, useValue: MAT_NATIVE_DATE_FORMATS}, ],
})
export class RentEditComponent implements OnInit {
    rent: Rent;

    constructor(
        public dialogRef: MatDialogRef<RentEditComponent>,
        @Inject(MAT_DIALOG_DATA) public data: any,
        private rentService: RentService
    ) {}

    ngOnInit(): void {
        this.rent = this.data.rent ? Object.assign({}, this.data.rent) : new Rent();
    }

    onSave() {
        this.rentService.saveRent(this.rent).subscribe(() => {
            this.dialogRef.close();
        });
    }

    onClose() {
        this.dialogRef.close();
    }
}