/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import type { Observable } from 'rxjs';
import type { CurriculumResponse } from '../models/CurriculumResponse';
import { OpenAPI } from '../core/OpenAPI';
import { request as __request } from '../core/request';
@Injectable({
  providedIn: 'root',
})
export class CurriculumService {
  constructor(public readonly http: HttpClient) {}
  /**
   * @param userExternalId
   * @returns CurriculumResponse OK
   * @throws ApiError
   */
  public getCurriculum(userExternalId?: string): Observable<CurriculumResponse> {
    return __request(OpenAPI, this.http, {
      method: 'GET',
      url: '/api/v1/users/me/curriculum',
      headers: {
        'User-External-Id': userExternalId,
      },
    });
  }
}
