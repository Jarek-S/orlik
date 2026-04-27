import { gql } from 'apollo-angular';
import { Injectable } from '@angular/core';
import * as Apollo from 'apollo-angular';
export type Maybe<T> = T | null;
export type InputMaybe<T> = Maybe<T>;
export type Exact<T extends { [key: string]: unknown }> = { [K in keyof T]: T[K] };
export type MakeOptional<T, K extends keyof T> = Omit<T, K> & { [SubKey in K]?: Maybe<T[SubKey]> };
export type MakeMaybe<T, K extends keyof T> = Omit<T, K> & { [SubKey in K]: Maybe<T[SubKey]> };
export type MakeEmpty<T extends { [key: string]: unknown }, K extends keyof T> = { [_ in K]?: never };
export type Incremental<T> = T | { [P in keyof T]?: P extends ' $fragmentName' | '__typename' ? T[P] : never };
/** All built-in and custom scalars, mapped to their actual values */
export type Scalars = {
  ID: { input: string; output: string; }
  String: { input: string; output: string; }
  Boolean: { input: boolean; output: boolean; }
  Int: { input: number; output: number; }
  Float: { input: number; output: number; }
};

/**  types */
export type DemoPlayer = {
  __typename?: 'DemoPlayer';
  id: Scalars['ID']['output'];
  name: Scalars['String']['output'];
  primaryPosition?: Maybe<Scalars['String']['output']>;
  speedRank?: Maybe<Scalars['Int']['output']>;
  techniqueRank?: Maybe<Scalars['Int']['output']>;
};

export type Group = {
  __typename?: 'Group';
  createdAt?: Maybe<Scalars['String']['output']>;
  id: Scalars['ID']['output'];
  name?: Maybe<Scalars['String']['output']>;
  owner: User;
  players: Array<Player>;
};

/**  enums */
export enum GroupRole {
  Admin = 'ADMIN',
  Owner = 'OWNER',
  Player = 'PLAYER',
  Selector = 'SELECTOR'
}

export type Match = {
  __typename?: 'Match';
  id: Scalars['ID']['output'];
  matchDate: Scalars['String']['output'];
  matchType: MatchType;
  participations: Array<MatchParticipation>;
  pitch?: Maybe<Pitch>;
  teamAScore?: Maybe<Scalars['Int']['output']>;
  teamBScore?: Maybe<Scalars['Int']['output']>;
};

export type MatchParticipation = {
  __typename?: 'MatchParticipation';
  assists: Scalars['Int']['output'];
  goals: Scalars['Int']['output'];
  isMvp?: Maybe<Scalars['Boolean']['output']>;
  playedAsGoalkeeper?: Maybe<Scalars['Boolean']['output']>;
  playerId: Scalars['ID']['output'];
  team: Team;
};

export type MatchSaveInput = {
  matchDate: Scalars['String']['input'];
  matchType: MatchType;
  pitchId: Scalars['ID']['input'];
};

export enum MatchType {
  Internal = 'INTERNAL',
  MixedExternal = 'MIXED_EXTERNAL',
  TeamExternal = 'TEAM_EXTERNAL'
}

export type MatchUpdateInput = {
  matchDate?: InputMaybe<Scalars['String']['input']>;
  matchType?: InputMaybe<MatchType>;
  participations?: InputMaybe<Array<ParticipationSaveInput>>;
  pitchId?: InputMaybe<Scalars['ID']['input']>;
  teamAScore?: InputMaybe<Scalars['Int']['input']>;
  teamBScore?: InputMaybe<Scalars['Int']['input']>;
};

export type Mutation = {
  __typename?: 'Mutation';
  createMatch: Match;
  updateMatch: Match;
};


export type MutationCreateMatchArgs = {
  groupId: Scalars['ID']['input'];
  matchData: MatchSaveInput;
};


export type MutationUpdateMatchArgs = {
  input: MatchUpdateInput;
  matchId: Scalars['ID']['input'];
};

export type ParticipationSaveInput = {
  assists?: InputMaybe<Scalars['Int']['input']>;
  goals?: InputMaybe<Scalars['Int']['input']>;
  playedAsGoalkeeper?: InputMaybe<Scalars['Boolean']['input']>;
  playerId: Scalars['ID']['input'];
  team: Team;
};

export type Pitch = {
  __typename?: 'Pitch';
  city: Scalars['String']['output'];
  id: Scalars['ID']['output'];
  name: Scalars['String']['output'];
};

export type Player = {
  __typename?: 'Player';
  birthYear?: Maybe<Scalars['Int']['output']>;
  canPlayAsGk: Scalars['Boolean']['output'];
  defenseSkillRank?: Maybe<Scalars['Int']['output']>;
  defenseWorkRate?: Maybe<Scalars['Int']['output']>;
  firstName: Scalars['String']['output'];
  group: Group;
  id: Scalars['ID']['output'];
  isAdmin: Scalars['Boolean']['output'];
  isCoach: Scalars['Boolean']['output'];
  isGoalkeeperToday: Scalars['Boolean']['output'];
  joinedAt?: Maybe<Scalars['String']['output']>;
  lastName?: Maybe<Scalars['String']['output']>;
  nickName?: Maybe<Scalars['String']['output']>;
  primaryPosition?: Maybe<PrimaryPosition>;
  speedRank?: Maybe<Scalars['Int']['output']>;
  stamina?: Maybe<Scalars['Int']['output']>;
  techniqueRank?: Maybe<Scalars['Int']['output']>;
  user?: Maybe<User>;
};

/**  inputs */
export type PlayerSaveInput = {
  birthYear?: InputMaybe<Scalars['Int']['input']>;
  firstName: Scalars['String']['input'];
  groupId: Scalars['ID']['input'];
  lastName?: InputMaybe<Scalars['String']['input']>;
  nickName?: InputMaybe<Scalars['String']['input']>;
  primaryPosition?: InputMaybe<PrimaryPosition>;
};

export enum PrimaryPosition {
  Att = 'ATT',
  Def = 'DEF',
  Gk = 'GK',
  Mid = 'MID'
}

/**  operations */
export type Query = {
  __typename?: 'Query';
  getDemoPlayers: Array<DemoPlayer>;
  getGroup?: Maybe<Group>;
  getMatch?: Maybe<Match>;
  getPlayerById?: Maybe<Player>;
  getPlayers: Array<Player>;
  /**  logged user */
  me?: Maybe<User>;
  /**  logged user groups */
  myGroups: Array<Group>;
};


/**  operations */
export type QueryGetGroupArgs = {
  id: Scalars['ID']['input'];
};


/**  operations */
export type QueryGetMatchArgs = {
  id: Scalars['ID']['input'];
};


/**  operations */
export type QueryGetPlayerByIdArgs = {
  id: Scalars['ID']['input'];
};

export enum Team {
  TeamA = 'TEAM_A',
  TeamB = 'TEAM_B'
}

export type User = {
  __typename?: 'User';
  createdAt?: Maybe<Scalars['String']['output']>;
  email: Scalars['String']['output'];
  id: Scalars['ID']['output'];
  keycloakId: Scalars['String']['output'];
  ownedGroups: Array<Group>;
  players: Array<Player>;
};

export type GetPlayersQueryVariables = Exact<{ [key: string]: never; }>;


export type GetPlayersQuery = { __typename?: 'Query', getDemoPlayers: Array<{ __typename?: 'DemoPlayer', id: string, name: string, primaryPosition?: string | null, techniqueRank?: number | null, speedRank?: number | null }> };

export const GetPlayersDocument = gql`
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

  @Injectable({
    providedIn: 'root'
  })
  export class GetPlayersGQL extends Apollo.Query<GetPlayersQuery, GetPlayersQueryVariables> {
    override document = GetPlayersDocument;
    
    constructor(apollo: Apollo.Apollo) {
      super(apollo);
    }
  }