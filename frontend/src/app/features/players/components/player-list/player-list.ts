import {MatDialog} from '@angular/material/dialog';
import {Component, effect, inject, OnInit} from '@angular/core';
import {PlayerService} from '../../../../core/services/player/player.service';

@Component({
  selector: 'app-player-list',
  standalone: true,
  imports: [],
  templateUrl: './player-list.html'
})
export class PlayerListComponent implements OnInit {
  private readonly dialog = inject(MatDialog);
  private readonly playerService = inject(PlayerService);
  players = this.playerService.players;


  constructor() {
    effect(() => {
      console.log('Aktualny stan zawodników w UI:', this.players());
    });
  }

  ngOnInit() {
    this.playerService.loadDemoPlayers();
  }

}
