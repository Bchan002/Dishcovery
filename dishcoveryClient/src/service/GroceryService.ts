import { HttpClient, HttpErrorResponse } from "@angular/common/http";
import { inject, Injectable } from "@angular/core";

import { catchError, Observable, tap, throwError } from "rxjs";
import { AddGroceryItem, GroceryItems } from "../app/model/models";
import { environmentProd } from "../app/environments/environment.prod";


@Injectable()
export class GroceryService {
    
    //Inject HttpClient Service
    private http = inject(HttpClient)
    
    protected POST_GROCERY_ITEMS: string =   `${environmentProd.apiUrl}/saveGrocery`
    protected FETCH_GROCERY_ITEMS: string = `${environmentProd.apiUrl}/fetchGrocery`
    protected DELETE_GROCERY_ITEMS: string = `${environmentProd.apiUrl}/deleteGrocery`

    // protected POST_GROCERY_ITEMS: string = "http://localhost:8080/saveGrocery"
    // protected FETCH_GROCERY_ITEMS: string = "http://localhost:8080/fetchGrocery"
    // protected DELETE_GROCERY_ITEMS: string = "http://localhost:8080/deleteGrocery"

    saveGroceryItem(saveGrocery:AddGroceryItem[]): Observable<any>{

        //const fcmToken = localStorage.getItem("fcmToken")
        return this.http.post<any>(this.POST_GROCERY_ITEMS, saveGrocery).pipe
        (
            catchError(this.handleError)
        )
    }


    fetchSavedGrocery() : Observable<GroceryItems[]> {

        return this.http.get<GroceryItems[]>(this.FETCH_GROCERY_ITEMS).pipe(
            tap((result)=> {
                console.info("this is ur result", result)
            }), 
            catchError(this.handleError)
        )

    }

    deleteSavedGrocery(itemId:string): Observable<string> {

        console.info("Your itemId to delete is ", itemId)

        return this.http.post<any>(this.DELETE_GROCERY_ITEMS,itemId).pipe(
            tap((result)=> {
                console.info("This is ur resposne ", result)
            }), 
            catchError(this.handleError)
        )
    }

    /**
         * 
         *  ERROR Handling
         */
    
        private handleError(error: HttpErrorResponse): Observable<any> {
        
                let errorMessage  = ""
                
                if(error.status==0){
        
                    // Client side error 
                    console.error("An error occured ", error.error)
                } else {
        
                    // Server side error 
                    // To access the custom message from your ApiError JSON object 
                    if(error.status===404){
                        errorMessage = error.error.message
                        console.info("Your error is ", errorMessage)
                    } 
        
                    if(error.status===401){
                        errorMessage = error.error.message
                    }
                }   
                
                // return an observabele with a user facing error message 
                return throwError( () => new Error (errorMessage)) 
        
            }

}