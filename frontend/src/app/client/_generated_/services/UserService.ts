/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import type { Observable } from 'rxjs';
import type { UserResponse } from '../models/UserResponse';
import { OpenAPI } from '../core/OpenAPI';
import { request as __request } from '../core/request';
@Injectable({
    providedIn: 'root',
})
export class UserService {
    constructor(public readonly http: HttpClient) {}
    /**
     * @param userExternalId
     * @returns UserResponse OK
     * @throws ApiError
     */
    public getAllUsers(
        userExternalId?: string,
    ): Observable<Array<UserResponse>> {
        return __request(OpenAPI, this.http, {
            method: 'GET',
            url: '/api/v1/users',
            headers: {
                'User-External-Id': userExternalId,
            },
        });
    }
}
