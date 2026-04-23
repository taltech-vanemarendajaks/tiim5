/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import type { Observable } from 'rxjs';
import type { CourseResponse } from '../models/CourseResponse';
import { OpenAPI } from '../core/OpenAPI';
import { request as __request } from '../core/request';
@Injectable({
    providedIn: 'root',
})
export class CourseService {
    constructor(public readonly http: HttpClient) {}
    /**
     * @param start
     * @param take
     * @param title
     * @param code
     * @param userExternalId
     * @returns CourseResponse OK
     * @throws ApiError
     */
    public getAllCourses(
        start: number = 1,
        take: number = 300,
        title?: string,
        code?: string,
        userExternalId?: string,
    ): Observable<Array<CourseResponse>> {
        return __request(OpenAPI, this.http, {
            method: 'GET',
            url: '/api/v1/courses',
            headers: {
                'User-External-Id': userExternalId,
            },
            query: {
                'start': start,
                'take': take,
                'title': title,
                'code': code,
            },
        });
    }
}
