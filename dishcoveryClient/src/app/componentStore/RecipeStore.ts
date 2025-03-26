import { inject, Injectable } from "@angular/core"
import { Recipe, SearchRecipe } from "../model/models"
import { ComponentStore } from "@ngrx/component-store"

import { catchError, Observable, of, switchMap, tap } from "rxjs"
import { RecipeService } from "../../service/RecipeService"

export interface RecipeState {
    //Get all the stored Recipes
    allRecipes: Recipe[]
    filteredRecipes: Recipe[]
    selectedRecipe: Recipe | null
    savedRecipe: Recipe[]
    errorMessage: string
    searchedRecipe: SearchRecipe[]

}

@Injectable()
export class RecipeStore extends ComponentStore<RecipeState> {

    private recipeSvc = inject(RecipeService); // Inject RecipeService using inject()

    // Sets the initial state of the store
    constructor() {
        const savedRecipes = localStorage.getItem("allRecipes");


        // Initialize state, fallback to empty arrays if no saved data
        super({
            allRecipes: savedRecipes ? JSON.parse(savedRecipes) : [],
            filteredRecipes: [],
            errorMessage: "",
            selectedRecipe: null,
            savedRecipe: [],
            searchedRecipe: []
        });

        // If no recipes are loaded, fetch from backend
        if (this.get().allRecipes.length === 0) {
            this.fetchAllRecipes();
        }
    }

    /**
     *  SELECTORS
     */
    readonly getAllRecipes = this.select((state) => state.allRecipes)

    //readonly getFilteredRecipe = this.select((state) => state.filteredRecipes)

    readonly getSearchedRecipes = this.select((state) => state.searchedRecipe)

    readonly getSavedRecipes = this.select((state) => state.savedRecipe)
    readonly getSavedRecipesCount = this.select((state)=> state.savedRecipe.length)

    readonly getErrorMessage = this.select((state) => state.errorMessage)

    readonly getFilterRecipeById = (id: any): any =>
        this.select(state => state.allRecipes.find(recipe => recipe.recipeId == id));

    readonly getFilterSavedRecipeById = (id: any): any =>
        this.select(state => state.savedRecipe.find(recipe => recipe.recipeId == id));

    readonly getSearchRecipeById = (id: any): any =>
        this.select(state => state.searchedRecipe.find(recipe => recipe.recipeId == id));


    readonly getFilterRecipe = (filterby: string) => {
        return this.select<Recipe[]>(
            (state) => state.allRecipes.filter(
                recipe => (recipe.category == filterby)
            )
        )
    }

    /**
     *  UPDATORS
     */

    // readonly filterRecipes = this.updater<string>((state, category: string) => ({
    //     // Means keeping the state the same 
    //     ...state,
    //     filteredRecipes: state.allRecipes.filter(
    //       (recipe) => recipe.category === category
    //     ),
    //   }))


    readonly removeErrorMessage = this.updater((state) => ({
        ...state,  // preserve existing state
        errorMessage: ""
    }));


    readonly clearSearchedRecipes = this.updater((state) => ({
        ...state,
        searchedRecipes: []
      }));

    /**
     *  SIDE EFFECT -> Fetch Pre-stored Recipe From Backend
     */
    readonly fetchAllRecipes = this.effect(() =>
        this.recipeSvc.fetchPopularRecipe().pipe(
            tap((recipes) => {
                this.patchState({
                    allRecipes: recipes,
                    filteredRecipes: recipes,
                    errorMessage: "",
                });
                // Save fetched recipes to localStorage
                localStorage.setItem("allRecipes", JSON.stringify(recipes));
            }),
            catchError((error) => {
                console.info("Your errorMessage is ", error.message)
                this.patchState({ errorMessage: error.message });
                return of([]);
            })
        )
    );



    // readonly fetchSearchedRecipes = this.effect((query$: Observable<string>) =>
    //     query$.pipe(
    //         switchMap(query => 
                
    //             this.recipeSvc.fetchSearchedRecipe(query).pipe(
    //                 tap(searchedRecipes => {
    //                     this.patchState({
    //                         searchedRecipe: searchedRecipes,
    //                         errorMessage: ""
    //                     });
    //                     localStorage.setItem("searchedRecipes", JSON.stringify(searchedRecipes));

    //                 }),
    //                 catchError(error => {

    //                     console.info("Your errorMessage is ", error.message)
    //                     this.patchState({
    //                         errorMessage: error.message,
    //                         searchedRecipe: []
    //                     });
    //                     return of([]); // Return an empty array if error occurs
    //                 })
    //             )
    //         )
    //     )
    // )

    readonly fetchSearchedRecipes = this.effect((query$: Observable<string>) =>
        query$.pipe(
          switchMap(query => {
            // Clear previous results before fetching
            this.patchState({
              searchedRecipe: [],
              errorMessage: ""
            });
            // ✅ RETURN the observable here
            return this.recipeSvc.fetchSearchedRecipe(query).pipe(
              tap(searchedRecipes => {
                this.patchState({
                  searchedRecipe: searchedRecipes,
                  errorMessage: ""
                });
                localStorage.setItem("searchedRecipes", JSON.stringify(searchedRecipes));
              }),
              catchError(error => {
                console.info("Your errorMessage is ", error.message);
                this.patchState({
                  errorMessage: error.message,
                  searchedRecipe: []
                });
                return of([]);
              })
            );
          })
        )
      );
      

    // In your RecipeStore class
    readonly fetchSavedRecipe = this.effect<void>(trigger$ =>
        trigger$.pipe(
            switchMap(() => this.recipeSvc.fetchSavedRecipe().pipe(
                tap((recipes) => {
                    console.log("🔍 API returned recipes:", recipes);
                    // Replace the entire array with fresh data from API
                    this.patchState({
                        savedRecipe: recipes
                    });
                    console.log("🔄 Store updated with:", this.get().savedRecipe.length, "recipes");
                }),
                catchError((error) => {
                    console.error("Error fetching saved recipes:", error);
                    this.patchState({ errorMessage: error.message });
                    return of([]);
                })
            ))
        )
    );





    // // Methods 
    // getRecipeDetailsById(recipeId: any): any {
    //   console.info("Your recipeID to find is ", recipeId);
    //   return this.get().allRecipes.find((recipe) => recipe.recipeId == recipeId);
    // }


}
