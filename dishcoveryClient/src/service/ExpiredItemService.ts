import { HttpClient } from "@angular/common/http";
import { inject, Injectable } from "@angular/core";
import { count, map, Observable, tap } from "rxjs";
import { ExpiredItem } from "../app/model/models";
import { environmentProd } from "../app/environments/environment.prod";

@Injectable()
export class ExpiredItemService{

    private http = inject(HttpClient)

    private FETCH_EXPIRED_ITEMS = `${environmentProd.apiUrl}/fetchExpiredItems`
    private CLEAR_EXPIRED_ITEMS = `${environmentProd.apiUrl}/clearExpiredItems`


    // private FETCH_EXPIRED_ITEMS = "http://localhost:8080/fetchExpiredItems"
    // private CLEAR_EXPIRED_ITEMS = "http://localhost:8080/clearExpiredItems"


    fetchExpiredItems(): Observable<ExpiredItem[]> {

        return this.http.get<any[]>(this.FETCH_EXPIRED_ITEMS).pipe(
            tap(result=>{
                console.info("Fetced expired items result ", result)
            }),
            map(result=>{
                
                return result.map(item=> {
                    const todayDate = new Date()
                    const expiryDate = new Date(item.expiredDate)
                    const differenceDate = expiryDate.getTime() - todayDate.getTime()
                    const countDaysBeforeExpiry = Math.ceil(differenceDate / (1000 * 60 * 60 * 24));

                    //Create a new object 
                    const expiredItem : ExpiredItem = {
                        expiredItemName: item.expiredItem,
                        daysBeforeExpiry: countDaysBeforeExpiry
                    }

                    return expiredItem

                })
            }), 
            tap(mappedResults => {
                console.info("This is your result after mapping ", mappedResults)
            })

        )
    }

    clearExpiredItems(): Observable<any> {

        return this.http.post<any>(this.CLEAR_EXPIRED_ITEMS,null).pipe(
            tap(response=>{
                console.info("this is ur response back from clearing expired items ", response)
            })
        )
    }
    
}