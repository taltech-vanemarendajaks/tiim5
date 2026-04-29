/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import type { Observable } from 'rxjs';
import type { RegisterUserRequest } from '../models/RegisterUserRequest';
import type { UserResponse } from '../models/UserResponse';
import { OpenAPI } from '../core/OpenAPI';
import { request as __request } from '../core/request';
@Injectable({
  providedIn: 'root',
})
export class UserService {
  constructor(public readonly http: HttpClient) {}
  /**
   * @param requestBody
   * @param userExternalId
   * @returns UserResponse OK
   * @throws ApiError
   */
  public createNewUser(
    requestBody: RegisterUserRequest,
    userExternalId?: string,
  ): Observable<UserResponse> {
    return __request(OpenAPI, this.http, {
      method: 'POST',
      url: '/api/v1/users/register',
      headers: {
        'User-External-Id': userExternalId,
      },
      body: requestBody,
      mediaType: 'application/json',
    });
  }
  /**
   * @param userExternalId
   * @returns UserResponse OK
   * @throws ApiError
   */
  public getAllUsers(userExternalId?: string): Observable<Array<UserResponse>> {
    return __request(OpenAPI, this.http, {
      method: 'GET',
      url: '/api/v1/users',
      headers: {
        'User-External-Id': userExternalId,
      },
    });
  }
  /**
   * @param userExternalId
   * @returns UserResponse OK
   * @throws ApiError
   */
  public getCurrentUser(userExternalId?: string): Observable<UserResponse> {
    return __request(OpenAPI, this.http, {
      method: 'GET',
      url: '/api/v1/users/me',
      headers: {
        'User-External-Id': userExternalId,
      },
    });
  }
}
