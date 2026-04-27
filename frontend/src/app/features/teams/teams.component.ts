import {ChangeDetectionStrategy, Component, Input} from '@angular/core';
import {DemoBalancedTeams} from '../../core/services/demo/demo-balancer.service';
import {DemoPlayer} from '../../graphql/generated';

@Component({
  selector: 'app-teams',
  imports: [],
  templateUrl: './teams.component.html',
  styleUrl: 'teams.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class TeamsComponent {
  @Input({required: true}) data!: DemoBalancedTeams;

  getPlayersByPos(team: DemoPlayer[], ...positions: string[]): DemoPlayer[] {
    return team.filter(p => positions.includes(p.primaryPosition as string));
  }
}
