import { Pageable } from "../../core/model/page/Pageable";
import { Rent } from "./Rent";

export class RentPage {
    content: Rent[];
    pageable: Pageable;
    totalElements: number;
}