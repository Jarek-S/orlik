import {Component, signal} from '@angular/core';
import {FootballGroup} from '../../shared/models/group.model';
import {
  MatCard,
  MatCardActions,
  MatCardContent,
  MatCardHeader,
  MatCardSubtitle,
  MatCardTitle
} from '@angular/material/card';
import {MatIcon} from '@angular/material/icon';
import {MatButton, MatIconButton} from '@angular/material/button';
import {TranslocoDirective} from '@jsverse/transloco';
import {MatChip, MatChipSet} from '@angular/material/chips';
import {MatTooltip} from '@angular/material/tooltip';
import {RouterLink} from '@angular/router';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  imports: [
    MatCardContent,
    MatIcon,
    MatCardActions,
    MatButton,
    MatCard,
    MatCardHeader,
    MatCardTitle,
    MatCardSubtitle,
    TranslocoDirective,
    MatChipSet,
    MatChip,
    MatIconButton,
    MatTooltip,
    RouterLink
  ],
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent {
  // mocked data for now
  public groups = signal<FootballGroup[]>([
    {
      id: '1',
      name: 'Orlik wtorki 20:00',
      memberCount: 22,
      lastMatchDate: '2026-02-24',
      location: 'Szczecin, ul. Paproci',
      isAdmin: true,
      isScout: false
    },
    {
      id: '2',
      name: 'Orlik Świętoborzyców',
      memberCount: 14,
      lastMatchDate: '2026-02-27',
      location: 'Szczecin, ul. Świętoborzyców',
      isAdmin: false,
      isScout: true
    },
    {
      id: '3',
      name: 'Stadion Piaskowa',
      memberCount: 16,
      lastMatchDate: '2026-02-27',
      location: 'Police, ul. Piaskowa',
      isAdmin: false,
      isScout: false
    }
  ]);

  getRoleLabel(group: FootballGroup): string {
    if (group.isAdmin) return 'role.admin';
    if (group.isScout) return 'role.scout';
    return 'role.player';
  }
}
