/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import type { Observable } from 'rxjs';
import type { CurriculumResponse } from '../models/CurriculumResponse';
import type { CurriculumVersionResponse } from '../models/CurriculumVersionResponse';
import { OpenAPI } from '../core/OpenAPI';
import { request as __request } from '../core/request';
@Injectable({
  providedIn: 'root',
})
export class CurriculumService {
  constructor(public readonly http: HttpClient) {}
  /**
   * @param curriculumId
   * @param curriculumVersionId
   * @param userExternalId
   * @returns CurriculumResponse OK
   * @throws ApiError
   */
  public saveNewCurriculum(
    curriculumId: string,
    curriculumVersionId: string,
    userExternalId?: string,
  ): Observable<CurriculumResponse> {
    return __request(OpenAPI, this.http, {
      method: 'POST',
      url: '/api/v1/curriculums/new',
      headers: {
        'User-External-Id': userExternalId,
      },
      query: {
        curriculumId: curriculumId,
        curriculumVersionId: curriculumVersionId,
      },
    });
  }
  /**
   * @param studyPlanExternalId
   * @param userExternalId
   * @returns CurriculumResponse OK
   * @throws ApiError
   */
  public getCurriculumByStudyPlan(
    studyPlanExternalId: string,
    userExternalId?: string,
  ): Observable<CurriculumResponse> {
    return __request(OpenAPI, this.http, {
      method: 'GET',
      url: '/api/v1/users/me/study-plan/{studyPlanExternalId}/curriculum',
      path: {
        studyPlanExternalId: studyPlanExternalId,
      },
      headers: {
        'User-External-Id': userExternalId,
      },
    });
  }
  /**
   * @param start
   * @param take
   * @param q
   * @param studyLevel
   * @param userExternalId
   * @returns CurriculumResponse OK
   * @throws ApiError
   */
  public getAllCurriculums(
    start: number = 1,
    take: number = 24,
    q?: string,
    studyLevel: string = 'bachelor',
    userExternalId?: string,
  ): Observable<Array<CurriculumResponse>> {
    return __request(OpenAPI, this.http, {
      method: 'GET',
      url: '/api/v1/curriculums',
      headers: {
        'User-External-Id': userExternalId,
      },
      query: {
        start: start,
        take: take,
        q: q,
        study_level: studyLevel,
      },
    });
  }
  /**
   * @param curriculumId
   * @param userExternalId
   * @returns CurriculumVersionResponse OK
   * @throws ApiError
   */
  public getVersionsForCurriculum(
    curriculumId: string,
    userExternalId?: string,
  ): Observable<Array<CurriculumVersionResponse>> {
    return __request(OpenAPI, this.http, {
      method: 'GET',
      url: '/api/v1/curriculums/{curriculumId}/versions',
      path: {
        curriculumId: curriculumId,
      },
      headers: {
        'User-External-Id': userExternalId,
      },
    });
  }
}
