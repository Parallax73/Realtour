export interface Unit {
  id: string;
  address: string | null;
  number: string;
  neighbourhood: string | null;
  city: string | null;
  price: number;
  realtor: SimplifiedRealtor;
}

export interface SimplifiedRealtor {
  id: string;
  username: string;
}
