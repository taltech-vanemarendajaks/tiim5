/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import type { Observable } from 'rxjs';
import type { PlannedCourseRequest } from '../models/PlannedCourseRequest';
import type { PlannedCourseResponse } from '../models/PlannedCourseResponse';
import { OpenAPI } from '../core/OpenAPI';
import { request as __request } from '../core/request';
@Injectable({
    providedIn: 'root',
})
export class PlannedCourseService {
    constructor(public readonly http: HttpClient) {}
    /**
     * @param studyPlanExternalId
     * @param requestBody
     * @param userExternalId
     * @returns PlannedCourseResponse OK
     * @throws ApiError
     */
    public setPlannedCourses(
        studyPlanExternalId: string,
        requestBody: Array<PlannedCourseRequest>,
        userExternalId?: string,
    ): Observable<Array<PlannedCourseResponse>> {
        return __request(OpenAPI, this.http, {
            method: 'PUT',
            url: '/api/v1/users/me/study-plan/{studyPlanExternalId}/planned-courses',
            path: {
                'studyPlanExternalId': studyPlanExternalId,
            },
            headers: {
                'User-External-Id': userExternalId,
            },
            body: requestBody,
            mediaType: 'application/json',
        });
    }
}
