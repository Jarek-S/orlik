import {ChangeDetectionStrategy, Component, computed, inject, OnInit, signal} from '@angular/core';
import {PlayerService} from '../../core/services/player/player.service';
import {MatButton, MatIconButton} from '@angular/material/button';
import {DemoBalancedTeams, DemoBalancerService} from '../../core/services/demo/demo-balancer.service';
import {MatIcon} from '@angular/material/icon';
import {TeamsComponent} from '../teams/teams.component';
import {MatCheckbox} from '@angular/material/checkbox';
import {TranslocoDirective, TranslocoPipe} from '@jsverse/transloco';
import {MatCard, MatCardActions, MatCardContent, MatCardHeader, MatCardTitle} from '@angular/material/card';
import {MatProgressBar} from '@angular/material/progress-bar';

@Component({
  selector: 'app-demo',
  imports: [
    MatButton,
    MatIcon,
    MatIconButton,
    TeamsComponent,
    MatCheckbox,
    TranslocoDirective,
    MatCardTitle,
    MatCardHeader,
    MatCard,
    MatCardContent,
    MatProgressBar,
    MatCardActions,
    TranslocoPipe
  ],
  templateUrl: './demo.component.html',
  styleUrl: './demo.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class DemoComponent implements OnInit {
  protected readonly playerService = inject(PlayerService);
  private readonly balancer = inject(DemoBalancerService);
  private selectedIds = signal<Set<string>>(new Set());
  teamsResult = signal<DemoBalancedTeams | null>(null);

  displayPlayers = computed(() => {
    return this.playerService.demoPlayers().map(p => ({
      ...p,
      isPresent: this.selectedIds().has(p.id)
    }));
  });

  selectedCount = computed(() => this.selectedIds().size);

  ngOnInit(): void {
    this.playerService.loadDemoPlayers();
  }

  togglePlayer(playerId: string) {
    this.selectedIds.update(set => {
      const newSet = new Set(set);
      if (newSet.has(playerId)) {
        newSet.delete(playerId);
      } else {
        newSet.add(playerId);
      }
      return newSet;
    });
  }

  onGenerate() {
    if (!this.isSelectionValid()) return;

    const allPlayers = this.playerService.demoPlayers();
    const selectedPlayers = allPlayers.filter(p => this.selectedIds().has(p.id));

    const result = this.balancer.createBalancedTeams(selectedPlayers);

    this.teamsResult.set(result);
  }

  isAllSelected = computed(() => {
    const allPlayers = this.playerService.demoPlayers();
    return allPlayers.length > 0 && this.selectedIds().size === allPlayers.length;
  });

  toggleAll() {
    const allPlayers = this.playerService.demoPlayers();

    if (this.isAllSelected()) {
      this.selectedIds.set(new Set());
    } else {
      const allIds = allPlayers.map(p => p.id);
      this.selectedIds.set(new Set(allIds));
    }
  }

  isSelectionValid = computed(() => {
    const count = this.selectedCount();
    const isEven = count % 2 === 0;
    const isInRange = count >= 16 && count <= 18;
    return isEven && isInRange;
  });

  isPlayerSelected(playerId: string) {
    return this.selectedIds().has(playerId);
  }
}
