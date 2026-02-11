import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IStravaAccount, NewStravaAccount } from '../strava-account.model';

export type PartialUpdateStravaAccount = Partial<IStravaAccount> & Pick<IStravaAccount, 'id'>;

export type EntityResponseType = HttpResponse<IStravaAccount>;
export type EntityArrayResponseType = HttpResponse<IStravaAccount[]>;

@Injectable({ providedIn: 'root' })
export class StravaAccountService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/strava-accounts');

  create(stravaAccount: NewStravaAccount): Observable<EntityResponseType> {
    return this.http.post<IStravaAccount>(this.resourceUrl, stravaAccount, { observe: 'response' });
  }

  update(stravaAccount: IStravaAccount): Observable<EntityResponseType> {
    return this.http.put<IStravaAccount>(`${this.resourceUrl}/${this.getStravaAccountIdentifier(stravaAccount)}`, stravaAccount, {
      observe: 'response',
    });
  }

  partialUpdate(stravaAccount: PartialUpdateStravaAccount): Observable<EntityResponseType> {
    return this.http.patch<IStravaAccount>(`${this.resourceUrl}/${this.getStravaAccountIdentifier(stravaAccount)}`, stravaAccount, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IStravaAccount>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IStravaAccount[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getStravaAccountIdentifier(stravaAccount: Pick<IStravaAccount, 'id'>): number {
    return stravaAccount.id;
  }

  compareStravaAccount(o1: Pick<IStravaAccount, 'id'> | null, o2: Pick<IStravaAccount, 'id'> | null): boolean {
    return o1 && o2 ? this.getStravaAccountIdentifier(o1) === this.getStravaAccountIdentifier(o2) : o1 === o2;
  }

  addStravaAccountToCollectionIfMissing<Type extends Pick<IStravaAccount, 'id'>>(
    stravaAccountCollection: Type[],
    ...stravaAccountsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const stravaAccounts: Type[] = stravaAccountsToCheck.filter(isPresent);
    if (stravaAccounts.length > 0) {
      const stravaAccountCollectionIdentifiers = stravaAccountCollection.map(stravaAccountItem =>
        this.getStravaAccountIdentifier(stravaAccountItem),
      );
      const stravaAccountsToAdd = stravaAccounts.filter(stravaAccountItem => {
        const stravaAccountIdentifier = this.getStravaAccountIdentifier(stravaAccountItem);
        if (stravaAccountCollectionIdentifiers.includes(stravaAccountIdentifier)) {
          return false;
        }
        stravaAccountCollectionIdentifiers.push(stravaAccountIdentifier);
        return true;
      });
      return [...stravaAccountsToAdd, ...stravaAccountCollection];
    }
    return stravaAccountCollection;
  }
}
