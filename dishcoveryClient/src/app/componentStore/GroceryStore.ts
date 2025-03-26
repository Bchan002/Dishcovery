import { inject, Injectable } from "@angular/core";
 
import { ComponentStore } from "@ngrx/component-store";
 
import { catchError, of, switchMap, tap } from "rxjs";

import { GroceryItems, shoppingList } from "../model/models";
import { GroceryService } from "../../service/GroceryService";

export interface GroceryState {

    savedGrocery: GroceryItems[]
    shoppingList: shoppingList[]
}

@Injectable()
export class GroceryStore extends ComponentStore<GroceryState> {

    // Inject Grocery Service
    private grocerySvc = inject(GroceryService)

    // Set initial State of the store 
    constructor() {

        // Initialize the state 
        super({
            savedGrocery: [], 
            shoppingList: []
        })
    }


    /*
     * SELECTORS 
     */
    readonly fetchGrocery = this.select((state) => state.savedGrocery)
    readonly getGroceryItems = this.select((state)=> state.savedGrocery.length)
    
    /*
     *  SIDE EFFECTS 
     */
    //  readonly fetchGroceryItems = this.effect(() =>
    //       this.grocerySvc.fetchSavedGrocery().pipe(
    //           tap((grocery) => {
    //               this.patchState({
    //                   savedGrocery: grocery
    //               });
                  
    //           }),
    //           catchError((error) => {
    //               console.info("Your errorMessage is ", error.message)
    //               return of([]);
    //           })
    //       )
    //   );

        readonly fetchGroceryItems = this.effect<void>(trigger$ => 
          trigger$.pipe(
            switchMap(() => this.grocerySvc.fetchSavedGrocery().pipe(
              tap((grocery) => {
                console.log("🔍 API returned groceries:", grocery);
                this.patchState({
                  savedGrocery: grocery
                });
                localStorage.setItem("groceryStore", JSON.stringify(grocery))
                console.log("🔄 Store updated with:", this.get().savedGrocery.length, "grcoery");
              }),
              catchError((error) => {
                console.error("Error fetching saved grocery", error);
                return of([]);
              })
            ))
          )
        )




}