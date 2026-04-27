import {inject, Injectable, signal} from '@angular/core';
import {Apollo, gql} from 'apollo-angular';
import {DemoPlayer, Player} from '../../../graphql/generated';

const GET_DEMO_PLAYERS = gql`
  query GetPlayers {
    getDemoPlayers {
      id
      name
      primaryPosition
      techniqueRank
      speedRank
    }
  }
`;

@Injectable({providedIn: 'root'})
export class PlayerService {
  private readonly apollo = inject(Apollo);

  private readonly playersState = signal<Player[]>([]);
  readonly players = this.playersState.asReadonly();

  private readonly demoPlayerState = signal<DemoPlayer[]>([]);
  readonly demoPlayers = this.demoPlayerState.asReadonly();

  loadDemoPlayers() {
    this.apollo.query<any>({query: GET_DEMO_PLAYERS})
      .subscribe(result => {
        const data = result.data?.getDemoPlayers ?? [];
        this.demoPlayerState.set(data);
      });
  }

}
