import { inject, Injectable } from "@angular/core";
import { BehaviorSubject, catchError, Observable, tap, throwError } from "rxjs";
import { HttpClient, HttpErrorResponse } from "@angular/common/http";
import { Recipe } from "../app/model/models";
import { environmentProd } from "../app/environments/environment.prod";

@Injectable()
export class AiRecipeService {

    private http = inject(HttpClient)
    
    private GET_AI_RECIPE = `${environmentProd.apiUrl}/getSuggestedRecipes`

    // private GET_AI_RECIPE = "http://localhost:8080/getSuggestedRecipes"; 

    suggestedRecipe = new BehaviorSubject<Recipe[]>([])

    fetchAIRecipe(ingredients:string[]): Observable<Recipe[]> {
        
        return this.http.post<Recipe[]>(this.GET_AI_RECIPE,ingredients).pipe(
            tap(result=>{
                console.info("Here is your result ", result)
                this.suggestedRecipe.next(result)
            }), 
            catchError(this.handleError)
        )
    }

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

            if(error.status===500){
                errorMessage = error.error.message
            }
        }   
        
        // return an observabele with a user facing error message 
        return throwError( () => new Error (errorMessage)) 

    }
}