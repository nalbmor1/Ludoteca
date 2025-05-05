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

@Component({
    selector: 'app-rent-edit',
    standalone: true,
    imports: [FormsModule, ReactiveFormsModule, MatFormFieldModule, MatInputModule, MatButtonModule, MatDatepickerModule, MatNativeDateModule],
    templateUrl: './rent-edit.component.html',
    styleUrl: './rent-edit.component.scss',
    providers: [ {provide: DateAdapter, useClass: NativeDateAdapter}, {provide: MAT_DATE_FORMATS, useValue: MAT_NATIVE_DATE_FORMATS}, { provide: MAT_DATE_LOCALE, useValue: 'en-GB' } ],
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
        const startDate = this.rent.startDate ? new Date(this.rent.startDate) : null;
        const endDate = this.rent.endDate ? new Date(this.rent.endDate) : null;

        if (startDate && endDate) {
            // Validar que la fecha de fin no sea anterior a la fecha de inicio
            if (startDate > endDate) {
                alert('La fecha de fin no puede ser anterior a la fecha de inicio.');
                return;
            }
    
            // Validar que el rango no exceda los 14 días
            const diffInDays = (endDate.getTime() - startDate.getTime()) / (1000 * 60 * 60 * 24);
            if (diffInDays > 14) {
                alert('El préstamo no puede exceder los 14 días.');
                return;
            }

            this.rent.startDate = this.formatDateToLocal(startDate) as any;
            this.rent.endDate = this.formatDateToLocal(endDate) as any;
        }
        
        this.rentService.saveRent(this.rent).subscribe(() => {
            this.dialogRef.close();
        });
    }

    onClose() {
        this.dialogRef.close();
    }

    private formatDateToLocal(date: Date): string {
        const year = date.getFullYear();
        const month = (date.getMonth() + 1).toString().padStart(2, '0'); // Los meses van de 0 a 11
        const day = date.getDate().toString().padStart(2, '0');
        return `${year}-${month}-${day}`;
    }
}