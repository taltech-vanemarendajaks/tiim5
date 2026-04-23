/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
export type CourseResponse = {
    externalId: string;
    oisExternalId: string;
    versionExternalId: string;
    titleEn: string;
    titleEt: string;
    code: string;
    credits: number;
    semesterType: CourseResponse.semesterType;
};
export namespace CourseResponse {
    export enum semesterType {
        SPRING = 'SPRING',
        AUTUMN = 'AUTUMN',
    }
}

