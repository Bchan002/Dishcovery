import { HttpClient, HttpErrorResponse, HttpHeaders, HttpParams } from "@angular/common/http";
import { inject, Injectable } from "@angular/core";
import { catchError, Observable, tap, throwError } from "rxjs";
import { Recipe, SearchRecipe } from "../app/model/models";
import { environmentProd } from "../app/environments/environment.prod";


@Injectable()
export class RecipeService {
    
    // Inject httpClient 
    private http = inject(HttpClient)
    private recipes: Recipe[] = []; 

    protected GET_POP_RECIPE: string = `${environmentProd.apiUrl}/recipe/popular`
    protected GET_SEARCHED_RECIPE: string = `${environmentProd.apiUrl}/recipe/search`
    protected SAVE_RECIPE: string = `${environmentProd.apiUrl}/recipe/save`
    protected GET_SAVED_RECIPES: string = `${environmentProd.apiUrl}/recipe/getSavedRecipe`
    protected DELETE_SAVED_RECIPES: string = `${environmentProd.apiUrl}/recipe/delete`

    // protected GET_POP_RECIPE: string = "http://localhost:8080/recipe/popular"
    // protected GET_SEARCHED_RECIPE: string = "http://localhost:8080/recipe/search"
    // protected SAVE_RECIPE: string = "http://localhost:8080/recipe/save"
    // protected GET_SAVED_RECIPES: string = "http://localhost:8080/recipe/getSavedRecipe"
    // protected DELETE_SAVED_RECIPES: string = "http://localhost:8080/recipe/delete"

    fetchPopularRecipe() : Observable<Recipe[]> {

        return this.http.get<Recipe[]>(this.GET_POP_RECIPE).pipe(
            tap(result=> {
                console.info("This is ur popular Recipe", result)
            }),
            catchError(this.handleError)
        )
    }


     fetchSearchedRecipe(query:string): Observable<SearchRecipe[]> {
     
             const queryParams = new HttpParams()
                 .set("query",query)

            console.info("Your service query is ", query)
                 
             return this.http.get<SearchRecipe[]>(this.GET_SEARCHED_RECIPE, {params: queryParams}).pipe(
                 tap(result => {
                     console.info("This is ur searched Recipe", result)
                 }), 
                 catchError( (error) => this.handleError(error))
             )
    }

    fetchSavedRecipe(): Observable<Recipe[]> {

        return this.http.get<Recipe[]>(this.GET_SAVED_RECIPES).pipe(
            tap(result=> {
                console.info("This is ur searched Recipe", result)
            }),
            catchError(this.handleError)
        )

    }

    saveRecipe(recipe:Recipe):Observable<any> {
        

        console.info("Sending Recipe", JSON.stringify(recipe))

        return this.http.post<any>(this.SAVE_RECIPE, recipe)
            .pipe(
                catchError(this.handleError)
            )
        
    }

    deleteRecipe(recipeId:number):Observable<any> {

        console.info("Deleteing recipe", recipeId)

        return this.http.post<any>(this.DELETE_SAVED_RECIPES,recipeId)
            .pipe(
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