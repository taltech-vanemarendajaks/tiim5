/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import type { Observable } from 'rxjs';
import type { SemesterResponse } from '../models/SemesterResponse';
import { OpenAPI } from '../core/OpenAPI';
import { request as __request } from '../core/request';
@Injectable({
    providedIn: 'root',
})
export class SemesterService {
    constructor(public readonly http: HttpClient) {}
    /**
     * @param studyPlanExternalId
     * @param userExternalId
     * @returns SemesterResponse OK
     * @throws ApiError
     */
    public getSemesters(
        studyPlanExternalId: string,
        userExternalId?: string,
    ): Observable<Array<SemesterResponse>> {
        return __request(OpenAPI, this.http, {
            method: 'GET',
            url: '/api/v1/users/me/study-plan/{studyPlanExternalId}/semesters',
            path: {
                'studyPlanExternalId': studyPlanExternalId,
            },
            headers: {
                'User-External-Id': userExternalId,
            },
        });
    }
}
