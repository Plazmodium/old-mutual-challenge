export interface ICountry {
  commonName: string;
  officialName: string;
  cca2: string;
  cca3: string;
  flagPng: string;
  flagSvg: string;
  capital: string;
  region: string;
  subregion: string;
  population: number;
}

export interface IPageable {
  pageNumber: number;
  pageSize: number;
  sort: {
    sorted: boolean;
    unsorted: boolean;
    empty: boolean;
  };
  offset: number;
  paged: boolean;
  unpaged: boolean;
}

export interface IPage<T> {
  content: T[];
  pageable: IPageable;
  totalPages: number;
  totalElements: number;
  last: boolean;
  size: number;
  number: number;
  sort: {
    sorted: boolean;
    unsorted: boolean;
    empty: boolean;
  };
  numberOfElements: number;
  first: boolean;
  empty: boolean;
}
