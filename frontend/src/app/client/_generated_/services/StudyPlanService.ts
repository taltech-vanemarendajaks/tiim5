/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import type { Observable } from 'rxjs';
import type { CreateNewStudyPlanRequest } from '../models/CreateNewStudyPlanRequest';
import type { CreateNewStudyPlanSemesterRequest } from '../models/CreateNewStudyPlanSemesterRequest';
import type { StudyPlanResponse } from '../models/StudyPlanResponse';
import { OpenAPI } from '../core/OpenAPI';
import { request as __request } from '../core/request';
@Injectable({
  providedIn: 'root',
})
export class StudyPlanService {
  constructor(public readonly http: HttpClient) {}
  /**
   * @param requestBody
   * @param userExternalId
   * @returns StudyPlanResponse OK
   * @throws ApiError
   */
  public addNewStudyPlan(
    requestBody: CreateNewStudyPlanRequest,
    userExternalId?: string,
  ): Observable<StudyPlanResponse> {
    return __request(OpenAPI, this.http, {
      method: 'POST',
      url: '/api/v1/users/me/study-plans/new',
      headers: {
        'User-External-Id': userExternalId,
      },
      body: requestBody,
      mediaType: 'application/json',
    });
  }
  /**
   * @param studyPlanExternalId
   * @param requestBody
   * @param userExternalId
   * @returns StudyPlanResponse OK
   * @throws ApiError
   */
  public addSemester(
    studyPlanExternalId: string,
    requestBody: CreateNewStudyPlanSemesterRequest,
    userExternalId?: string,
  ): Observable<StudyPlanResponse> {
    return __request(OpenAPI, this.http, {
      method: 'POST',
      url: '/api/v1/users/me/study-plan/{studyPlanExternalId}/semesters/new',
      path: {
        studyPlanExternalId: studyPlanExternalId,
      },
      headers: {
        'User-External-Id': userExternalId,
      },
      body: requestBody,
      mediaType: 'application/json',
    });
  }
  /**
   * @param userExternalId
   * @returns StudyPlanResponse OK
   * @throws ApiError
   */
  public getStudyPlans(userExternalId?: string): Observable<Array<StudyPlanResponse>> {
    return __request(OpenAPI, this.http, {
      method: 'GET',
      url: '/api/v1/users/me/study-plans',
      headers: {
        'User-External-Id': userExternalId,
      },
    });
  }
}
