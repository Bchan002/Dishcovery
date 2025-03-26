import { inject, Injectable, OnDestroy } from "@angular/core";

import { Observable, Subscription, tap } from "rxjs";
import { HttpClient } from "@angular/common/http";
import { GroceryItems, shoppingList } from "../app/model/models";
import { GroceryStore } from "../app/componentStore/GroceryStore";
import { environmentProd } from "../app/environments/environment.prod";

@Injectable()
export class ShoppingListService {

    private groceryStore = inject(GroceryStore)
    private http = inject(HttpClient)

    private groceryStoreSub$!: Subscription

    private groceryList!: GroceryItems[]
    private finalShoppingList!: shoppingList
    private addIngredients:string[] = []

    private SAVE_SHOPPINGLIST = `${environmentProd.apiUrl}/saveShoppingList`
    private FETCH_SHOPPINGLIST = `${environmentProd.apiUrl}/fetchShoppingList`
     private DELETE_SHOPPINGLIST = `${environmentProd.apiUrl}/deleteShoppingList`

    // private SAVE_SHOPPINGLIST = "http://localhost:8080/saveShoppingList"
    // private FETCH_SHOPPINGLIST = "http://localhost:8080/fetchShoppingList"
    //  private DELETE_SHOPPINGLIST = "http://localhost:8080/deleteShoppingList"

    checkGroceryItems(recipe: shoppingList) {

        console.info("Adding recipe to shopping list:", recipe.recipeName);
        
        //Get the grocery from localstorage
        this.groceryList = JSON.parse(localStorage.getItem("groceryStore") || "[]");

         //Reset the finalShoppingList
         const shoppingList: shoppingList={
            recipeName:"",
            recipeImage:"",
            ingredients: this.addIngredients = []
        }
        this.finalShoppingList = shoppingList

        this.groceryList.forEach(a=> {
            console.info("Your grocery items are ", a.itemName)
        })

        recipe.ingredients.forEach( a=> {
            console.info("Your recipe ingredient are ", a)
        })

            //Check for duplicate items
            const ingredientExists = recipe.ingredients.forEach(
                (newIngredient => {

                    const same =this.groceryList.some( (storedIngredients)=> {
                        return storedIngredients.itemName.toLowerCase() == newIngredient.toLowerCase()
                    })
                    console.info("your result is ", same)
                    // if not same, add to final shopping list
                    if(!same){
                       
                        this.addIngredients = [...this.addIngredients,newIngredient]
                    }
                })
            )

            //Create a new 
            const finalShoppingList: shoppingList = {
                recipeName: recipe.recipeName,
                recipeImage: recipe.recipeImage, 
                ingredients: this.addIngredients
            }
            console.info("Your final shopping list is ", finalShoppingList)

            this.finalShoppingList = finalShoppingList

            //Send to service
            this.saveShoppingList(this.finalShoppingList).subscribe()
            

        
        

    }

    saveShoppingList(shopList:shoppingList):Observable<any>{
        console.info("Sending shopping List to backend.... ")

        return this.http.post<any>(this.SAVE_SHOPPINGLIST, shopList).pipe(
            tap(response=>{
                console.info("Your response is ", response)
            })
        )
    }


    fetchShoppingList():Observable<shoppingList[]>{
        console.info("fetching shopping List")

        return this.http.get<shoppingList[]>(this.FETCH_SHOPPINGLIST).pipe(
            tap(response=>{
                console.info("Your response is ", response)
            })
        )
    }

    deleteShoppingList(shoppingListId:string):Observable<any> {

        return this.http.post<any>(this.DELETE_SHOPPINGLIST, shoppingListId).pipe(
            tap(response=>{
                console.info("Your response is ", response)
            })
        )
    }




  

}