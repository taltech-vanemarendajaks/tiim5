import { CurriculumResponse } from '@/client';

export function mapStudyLevel(studyLevel: CurriculumResponse.studyLevel): string {
  const studyLevelKeys: Record<CurriculumResponse.studyLevel, string> = {
    [CurriculumResponse.studyLevel.BACHELOR]: 'Curriculums.Bachelor',
    [CurriculumResponse.studyLevel.MASTER]: 'Curriculums.Master',
    [CurriculumResponse.studyLevel.DOCTOR]: 'Curriculums.Doctor',
    [CurriculumResponse.studyLevel.INTEGRATED]: 'Curriculum.Integrated',
  };

  return studyLevelKeys[studyLevel] ?? studyLevel;
}
