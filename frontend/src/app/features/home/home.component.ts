import {Component, inject} from '@angular/core';
import {MatIcon} from '@angular/material/icon';
import {
  MatCard,
  MatCardActions,
  MatCardContent,
  MatCardHeader,
  MatCardSubtitle,
  MatCardTitle
} from '@angular/material/card';
import {MatButton} from '@angular/material/button';
import {RouterLink} from '@angular/router';
import {AuthService} from '../../core/services/auth/auth.service';
import {TranslocoDirective} from '@jsverse/transloco';

@Component({
  selector: 'app-home',
  imports: [
    MatIcon,
    MatCardActions,
    MatCardContent,
    MatCardTitle,
    MatCardSubtitle,
    MatCardHeader,
    MatCard,
    MatButton,
    RouterLink,
    TranslocoDirective
  ],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css',
})
export class HomeComponent {
  protected readonly auth = inject(AuthService);
}
