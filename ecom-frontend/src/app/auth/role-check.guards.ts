import { inject, PLATFORM_ID } from "@angular/core";
import { ActivatedRouteSnapshot, CanActivateFn, RouterStateSnapshot } from "@angular/router";
import { Oauth2 } from "./oauth2";
import { isPlatformBrowser } from "@angular/common";
import { interval, map, timeout,filter } from "rxjs";


export const roleCheckGuard:CanActivateFn= (next: ActivatedRouteSnapshot,
    state:RouterStateSnapshot) =>{
        const oauth2Service=inject(Oauth2);
        const platfromId=inject(PLATFORM_ID);

        if(isPlatformBrowser(platfromId)){
            const authorities=next.data['authorities'];
            return interval(50).pipe(
                filter(()=>oauth2Service.connectedUserQuery?.isPending() == false),
                map(()=>oauth2Service.connectedUserQuery?.data()),
                map(data=> !authorities || authorities.length === 0 || oauth2Service.hasAnyAuthorities(data! , authorities)),
                timeout(3000)
            )
        }else{
            return false;
        }
    } 