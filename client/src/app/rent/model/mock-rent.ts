import { RentPage } from './RentPage';

export const RENT_DATA: RentPage = {
    content: [
        { id: 1, startDate: new Date('2023-01-01'), endDate: new Date('2023-01-05'), game: { id: 1, title: 'Juego 1', age: 6, category: { id: 1, name: 'Categoría 1' }, author: { id: 1, name: 'Autor 1', nationality: 'Nacionalidad 1' }}, client: { id: 1, name: 'Cliente 1'} },
        { id: 2, startDate: new Date('2023-04-02'), endDate: new Date('2023-04-06'), game: { id: 2, title: 'Juego 2', age: 8, category: { id: 1, name: 'Categoría 1' }, author: { id: 2, name: 'Autor 2', nationality: 'Nacionalidad 2' }}, client: { id: 2, name: 'Cliente 2'} },
        { id: 3, startDate: new Date('2023-06-03'), endDate: new Date('2023-06-07'), game: { id: 3, title: 'Juego 3', age: 4, category: { id: 1, name: 'Categoría 1' }, author: { id: 3, name: 'Autor 3', nationality: 'Nacionalidad 3' }}, client: { id: 3, name: 'Cliente 3'} },
        { id: 4, startDate: new Date('2023-10-04'), endDate: new Date('2023-10-08'), game: { id: 4, title: 'Juego 4', age: 10, category: { id: 2, name: 'Categoría 2' }, author: { id: 1, name: 'Autor 1',  nationality: 'Nacionalidad 1' }}, client: { id: 4, name: 'Cliente 4'} },
    ],
    pageable: {
        pageSize: 5,
        pageNumber: 0,
        sort: [{ property: 'id', direction: 'ASC' }],
    },
    totalElements: 4,
};