import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Unit } from '@app/models/unit';
import { map, Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UnitService {
  private apiUrl = 'http://localhost:8080/api/v1/units';
  constructor(private http: HttpClient) { }




  getUnitById(id: string): Observable<Unit> {
  return this.http.get<any>(`${this.apiUrl}/${id}`).pipe(
    map(data => ({
      id: data.id,
      address: data.address,
      number: data.number,
      neighbourhood: data.neighbourhood,
      city: data.city,
      price: data.price,
      realtor: {
        id: data.realtor.id,
        username: data.realtor.username
      }
    }))
  );
}






}


