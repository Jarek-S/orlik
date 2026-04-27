export interface FootballGroup {
  id: string;
  name: string;
  memberCount: number;
  lastMatchDate?: string;
  location: string;
  isAdmin: boolean;
  isScout: boolean;
}
