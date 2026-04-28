/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import type { Observable } from 'rxjs';
import type { ModuleResponse } from '../models/ModuleResponse';
import { OpenAPI } from '../core/OpenAPI';
import { request as __request } from '../core/request';
@Injectable({
  providedIn: 'root',
})
export class ModuleService {
  constructor(public readonly http: HttpClient) {}
  /**
   * @param studyPlanExternalId
   * @param userExternalId
   * @returns ModuleResponse OK
   * @throws ApiError
   */
  public getModules(
    studyPlanExternalId: string,
    userExternalId?: string,
  ): Observable<Array<ModuleResponse>> {
    return __request(OpenAPI, this.http, {
      method: 'GET',
      url: '/api/v1/users/me/study-plan/{studyPlanExternalId}/modules',
      path: {
        studyPlanExternalId: studyPlanExternalId,
      },
      headers: {
        'User-External-Id': userExternalId,
      },
    });
  }
}
