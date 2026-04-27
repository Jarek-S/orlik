import {Injectable} from '@angular/core';
import {DemoPlayer, PrimaryPosition} from '../../../graphql/generated';

export interface DemoBalancedTeams {
  teamA: DemoPlayer[];
  teamB: DemoPlayer[];
  powerA: number;
  powerB: number;
}

@Injectable({
  providedIn: 'root',
})
export class DemoBalancerService {

  createBalancedTeams(players: DemoPlayer[]): DemoBalancedTeams {
    const teamA: DemoPlayer[] = [];
    const teamB: DemoPlayer[] = [];

    // 1. Separate goalkeepers
    const allGoalkeepers = players.filter(p => p.primaryPosition === PrimaryPosition.Gk);
    const allFieldPlayers = players.filter(p => p.primaryPosition !== PrimaryPosition.Gk)
      .sort((a, b) => this.getPlayerPower(a) - this.getPlayerPower(b));

    // 2. Draw the goalkeepers (in pairs)
    for (let i = 0; i < allGoalkeepers.length; i += 2) {
      const gk1 = allGoalkeepers[i];
      const gk2 = allGoalkeepers[i + 1];

      if (gk2) {
        if (Math.random() > 0.5) {
          teamA.push(gk1);
          teamB.push(gk2);
        } else {
          teamA.push(gk2);
          teamB.push(gk1);
        }
      } else {
        teamA.push(gk1);
      }
    }

    // 3. Draw the players (in pairs)
    for (let i = 0; i < allFieldPlayers.length; i += 2) {
      const p1 = allFieldPlayers[i];
      const p2 = allFieldPlayers[i + 1];

      if (p2) {
        // decide which player from each ‘equal’ pair will join which team
        if (Math.random() > 0.5) {
          teamA.push(p1);
          teamB.push(p2);
        } else {
          teamA.push(p2);
          teamB.push(p1);
        }
      } else {
        // for last player if odd field players
        if (teamA.length <= teamB.length) {
          teamA.push(p1);
        } else {
          teamB.push(p1);
        }
      }
    }

    return {
      teamA,
      teamB,
      powerA: this.calculateTeamPower(teamA),
      powerB: this.calculateTeamPower(teamB)
    };
  }

  private calculateTeamPower(team: DemoPlayer[]): number {
    return team.reduce((sum, p) => sum + (p.techniqueRank || 0) + (p.speedRank || 0), 0);
  }

  private getPlayerPower(player: DemoPlayer) {
    return player.techniqueRank! + player.speedRank!;
  }

}
