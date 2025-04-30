import { Game } from '../../game/model/Game';
import { Client } from '../../client/model/Client';

export class Rent {
    id: number;
    startDate: Date;
    endDate: Date;
    game: Game;
    client: Client;
}