import { IMember, NewMember } from './member.model';

export const sampleWithRequiredData: IMember = {
  id: 26797,
  firstName: 'Adolphie',
  lastName: 'Brun',
  email: 'Basile.Fernandez48@hotmail.fr',
};

export const sampleWithPartialData: IMember = {
  id: 30382,
  firstName: 'Gaëlle',
  lastName: 'Pons',
  email: 'Bohemond93@gmail.com',
};

export const sampleWithFullData: IMember = {
  id: 30531,
  firstName: 'Falba',
  lastName: 'Remy',
  email: 'Priscille_Menard@gmail.com',
};

export const sampleWithNewData: NewMember = {
  firstName: 'Arcade',
  lastName: 'Marchand',
  email: 'Vigile_Louis66@yahoo.fr',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
