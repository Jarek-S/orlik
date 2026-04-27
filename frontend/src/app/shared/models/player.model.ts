import {Player} from '../../graphql/generated';

export enum PrimaryPosition {
  GK = 'GK',
  DEF = 'DEF',
  MID = 'MID',
  ATT = 'ATT'
}

export interface PlayerUI extends Player {
  isPresent?: boolean;
}

export interface PlayerCard extends Player {
  // additional data for UI
  isHighlighted?: boolean;
  rating: number;
  gamesPlayed: number;
}
