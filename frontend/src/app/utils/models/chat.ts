import {Message} from '@app/utils/models/message'

export interface Chat {
  id: string;
  clientEmail: string;
  clientName: string;
  realtorEmail: string;
  realtorName: string;
  unitId: string;
  unitAddress: string | null;
  unitNumber: string;
  messages: Message[];
}