import { Component, inject, OnDestroy, OnInit } from '@angular/core';
import { ShoppingListService } from '../../../service/ShoppingListService';
import { PriceCompareService } from '../../../service/PriceComparisonService';
import { Router } from '@angular/router';
import { MatDialog } from '@angular/material/dialog';
import { Subscription } from 'rxjs';
import { PriceCompare, shoppingList } from '../../model/models';
import { ComparePriceComponent } from '../ComparePriceComponent/compare-price.component';

@Component({
  selector: 'app-shopping-list',
  standalone: false,
  templateUrl: './shopping-list.component.html',
  styleUrl: './shopping-list.component.css'
})
export class ShoppingListComponent implements OnInit,OnDestroy {

    //Inject 
    private shoppingListSvc = inject(ShoppingListService)
    private priceCompareSvc = inject(PriceCompareService)
    private priceDialog = inject(MatDialog)
    private router = inject(Router)

    private sub!:Subscription
    protected shoppingList!: shoppingList[]
   
    expandedIndex: number | null = null;


    ngOnInit(): void {
      
      this.getShoppingList()


    }

    getShoppingList():void{
      this.sub = this.shoppingListSvc.fetchShoppingList().subscribe({
        next: (result=>{
          console.info("Your result is ",result)
          this.shoppingList = result
        })
      })
    }

    ngOnActivate():void{

       this.getShoppingList()
    }

    deleteItem(index:number) {

      //Get the id 
      const shopListId = this.shoppingList[index].shopListId

      console.info("This is your shopList Id to delete ", shopListId)
      
      //Send to Service to delete the item 
      this.shoppingListSvc.deleteShoppingList(shopListId!).subscribe()
      this.shoppingList.splice(index, 1);

      if (this.expandedIndex === index) {
        this.expandedIndex = null;
      } else if (this.expandedIndex !== null && index < this.expandedIndex) {
        this.expandedIndex--; // Adjust index after deletion
      }
    }

    toggle(index: number) {
      this.expandedIndex = this.expandedIndex === index ? null : index;
    }

    comparePrices(index: number): void {
      // Get the ingredients and the max and minimum prices
      console.info(`Comparing prices for ${this.shoppingList[index].recipeName}`);

      console.info("here are your ingredients ", this.shoppingList[index].ingredients)
      
      const dialogRef = this.priceDialog.open(ComparePriceComponent, {
        width: '500px'
      });

      dialogRef.afterClosed().subscribe({
        next:(result)=>{
          console.info("Here is your prices ", result)

          //Create a new priceComparison Object 
          const priceCompare: PriceCompare = {
            ingredients: this.shoppingList[index]?.ingredients,
            minPrice : "$" + result.minPrice,
            maxPrice: "$" + result.maxPrice
          }

          console.info("Here is your query to compare price ", priceCompare)

          //Send to service 
          this.priceCompareSvc.checkPrice(priceCompare).subscribe()

          //Navigate 
          this.router.navigate(['/layout/comparePriceResult'])

        }
      })



      
    }
  


    ngOnDestroy(): void {
        this.sub?.unsubscribe()
    }
}
