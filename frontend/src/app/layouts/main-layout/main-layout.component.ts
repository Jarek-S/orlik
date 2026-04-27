import {Component, inject} from '@angular/core';
import {MatToolbar} from '@angular/material/toolbar';
import {MatIcon} from '@angular/material/icon';
import {RouterLink, RouterOutlet} from '@angular/router';
import {MatButton} from '@angular/material/button';
import {AuthService} from '../../core/services/auth/auth.service';
import {TranslocoDirective, TranslocoService} from '@jsverse/transloco';

@Component({
  selector: 'app-main-layout',
  imports: [
    MatToolbar,
    MatIcon,
    RouterOutlet,
    RouterLink,
    MatButton,
    TranslocoDirective,
  ],
  templateUrl: './main-layout.component.html',
  styleUrl: './main-layout.component.css',
})
export class MainLayoutComponent {
  private translationService = inject(TranslocoService);
  protected auth = inject(AuthService);

  public activeLang = this.translationService.getActiveLang();

  setLanguage(lang: string) {
    this.translationService.setActiveLang(lang);
    this.activeLang = lang;
  }
}
