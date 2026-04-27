import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatCheckboxModule } from '@angular/material/checkbox';

@Component({
  selector: 'app-player-form',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    MatDialogModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatCheckboxModule
  ],
  templateUrl: './player-form.html'
})
export class PlayerFormComponent {
  private dialogRef = inject(MatDialogRef<PlayerFormComponent>);

  playerName = '';
  isShadow = false;

  onCancel(): void {
    this.dialogRef.close();
  }

  onSubmit(): void {
    if (this.playerName) {
      this.dialogRef.close({ name: this.playerName, isShadow: this.isShadow });
    }
  }
}
